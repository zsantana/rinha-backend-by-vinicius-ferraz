package org.acme;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PessoaResource {

    private static final Logger logger = LoggerFactory.getLogger(PessoaResource.class);

    private static final Response RESPONSE_400 = Response.status(Response.Status.BAD_REQUEST).build();
    private static final Response RESPONSE_422 = Response.status(422).build();

    static Set<String> apelidos = new HashSet<>(100_000); //Total de registros do teste de stress
    static Map<UUID, PessoaCache> pessoaCache = new HashMap<>(100_000);


    @POST
    @Path("pessoas")
    public Uni<Response> post(PessoaDTO pessoaDTO) throws JsonMappingException, JsonProcessingException, SQLException {
        
        validarPessoa(pessoaDTO);

        var uuid = UUID.randomUUID();
        logger.info("Gerando UUID: {}", uuid);

        pessoaDTO.id = uuid;
        PessoaCache pessoaCache = PessoaCache.of(pessoaDTO);

        synchronized(PessoaResource.pessoaCache) {
            PessoaResource.pessoaCache.put(pessoaDTO.id, pessoaCache);
            PessoaResource.apelidos.add(pessoaDTO.apelido);
        }

        Pessoa pessoa = Pessoa.of(pessoaDTO);
        return Panache
                .withTransaction(pessoa::persist)
                .replaceWith(Response.status(Status.CREATED).entity(pessoa)
                .header("Location", "/pessoas/" + uuid.toString())
                .build());
    }


    private void validarPessoa(PessoaDTO ip) {

        if (ip.nome == null || ip.nascimento == null || ip.apelido == null ||
            ip.nome.length() > 100 || ip.apelido.length() > 32
            ) {
            throw new WebApplicationException(RESPONSE_422);
        }


        if (apelidos.contains(ip.apelido)) {
            throw new WebApplicationException(RESPONSE_422);
        }

        if (ip.stack != null) {
            for (int i = 0; i < ip.stack.length; i++) {
                if (ip.stack[i].length() > 32) {
                    throw new WebApplicationException(RESPONSE_422);
                }
            }
        }
    }

    @GET
    @Path("pessoas")
    @WithSession
    public Uni<List<Pessoa>>  getAll(@QueryParam("t") String termo) throws SQLException {
        
        if (termo == null || "".equals(termo)) {
            throw new WebApplicationException(RESPONSE_400);
        }

        Uni<List<Pessoa>> r = Pessoa.find("busca like '%' || ?1 || '%'", termo).page(0, 50).list();
        return r;
        
        // var t =  PessoaCache.search(termo);

        // // Busca no cache, senão busca no banco
        // if (t != null){
        //     return Uni.createFrom().item(Response.ok(t).build());
        // }else{
        //     var r = Pessoa.find("busca like '%' || ?1 || '%'", termo).page(0, 50).list();
            
        //     if (r != null)
        //         return Uni.createFrom().item(Response.ok(r).build()); 
        //     else
        //         return Uni.createFrom().item(Response.status(Status.NOT_FOUND).build());
        // }
        
    }

    @GET
    @Path("pessoas/{id}")
    @WithSession
    public Uni<Response> get(UUID id) throws SQLException {
        
        var pessoa = pessoaCache.get(id);

        if (pessoa != null){
            return Uni.createFrom().item(Response.ok(pessoa).build());
        }else{
            var uniPessoa = Pessoa.findById(id);

            if (uniPessoa != null)
                return Uni.createFrom().item(Response.ok(pessoa).build());
            else    
                return Uni.createFrom().item(Response.status(Status.NOT_FOUND).build());
        }
        
    }

    @GET
    @Path("contagem-pessoas")
    @WithSession
    public Uni<Long> count() throws SQLException {
        return Pessoa.count();
    }


    

}

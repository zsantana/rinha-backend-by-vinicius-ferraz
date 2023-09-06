package org.acme;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PessoaCache {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UUID id;
    public String apelido;
    public String nome;
    public String nascimento;
    public String[] stack;

    public static PessoaCache of(Pessoa p) {
        PessoaCache pessoa = PessoaResource.pessoaCache.get(p.id);
        if(pessoa != null) {
            return pessoa;
        }
        PessoaCache pc = new PessoaCache();
        pc.apelido = p.apelido;
        
        pc.id = p.id;
        pc.nome = p.nome;
        
        pc.nascimento = p.nascimento.format(formatter);
        if(p.stack != null) {
            String joinedMinusBrackets = p.stack.substring( 1, p.stack.length() - 1);
            pc.stack = joinedMinusBrackets.split( ", "); 
        }
        PessoaResource.pessoaCache.put(p.id, pc);
        return pc;
    }

    public static PessoaCache of(PessoaDTO p) {
        PessoaCache pc = new PessoaCache();
        pc.apelido = p.apelido;
        
        pc.id = p.id;
        pc.nome = p.nome;
        
        pc.nascimento = p.nascimento.format(formatter);
        pc.stack = p.stack; 
        return pc;
    }

    public static synchronized List<PessoaCache> search(String termo) {
        return PessoaResource.pessoaCache.values().stream()
                .filter(p -> p.apelido.contains(termo) 
                    || p.nome.contains(termo) 
                    || containsInStack(p.stack, termo) 
                )
                .limit(50)
                .collect(Collectors.toList());
    }

    private static boolean containsInStack(String[] stack, String termo) {
        for (String item : stack) {
            if (item.contains(termo)) {
                return true; // Se encontrou o termo em algum item do array, retorna true
            }
        }
        return false; // Se não encontrou o termo em nenhum item do array, retorna false
    }


    @Override
    public String toString() {
        return "PessoaCache [id=" + id + ", apelido=" + apelido + ", nome=" + nome + ", nascimento="
                + nascimento + ", stack=" + Arrays.toString(stack) + "]";
    }
    
}

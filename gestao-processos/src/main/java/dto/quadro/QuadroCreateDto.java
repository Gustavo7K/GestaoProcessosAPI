package dto.quadro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class QuadroCreateDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    public QuadroCreateDto(){
    }

    public @NotBlank @Size(max = 100) String getNome() { //algo a se analisar esse @NotBlank e @Size ser passado no getter
        return nome;
    }

    public void setNome(@NotBlank @Size(max = 100) String nome) {
        this.nome = nome;
    }
}

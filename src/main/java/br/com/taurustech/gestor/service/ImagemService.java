package br.com.taurustech.gestor.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;


@Service @RequiredArgsConstructor
public class ImagemService {

    String pasta = "uploads/";

    private byte[] validarStringImagem64(String imagemBase64) throws IOException {
        byte[] decodedBytes;
        try {
            decodedBytes = Base64.decodeBase64(imagemBase64.getBytes());
        } catch (IllegalArgumentException e) {
            throw new IOException();
        }
        return decodedBytes;
    }

    public String cadastrar(String imagemBase64) {
        return atualizar(imagemBase64,null);
    }

    public String atualizar(String imagemBase64, String idNomeImagem) {
        String retorno = null;

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(validarStringImagem64(imagemBase64));
            BufferedImage imagem = ImageIO.read(bis);
            String nomeImagemFinal ;
            if (idNomeImagem==null ){
                do{
                    nomeImagemFinal = UUID.randomUUID() + ".png";
                    existeImagemMemoriaPorNome(nomeImagemFinal);
                }while (!existeImagemMemoriaPorNome(nomeImagemFinal));
            } else {
                if (!deletarImagemMemoria(idNomeImagem)) gerarErroValidation("imagem", "não foi possivel atualizar");
                nomeImagemFinal = idNomeImagem;
            }

            retorno = nomeImagemFinal;

            File outputFile = new File(pasta, nomeImagemFinal);


            ImageIO.write(imagem, "png", outputFile);

        } catch (IOException e) {
            gerarErroValidation("imagem", "inválida");
        }
        return retorno;
    }

    public Path getCaminho (String nome){
        return Paths.get(pasta + nome).toAbsolutePath();
    }
    private boolean deletarImagemMemoria (String nome){

        File imagem = new File(String.valueOf(getCaminho(nome)));
        if (imagem.exists()) {
            try {
                Files.delete(imagem.toPath());
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean existeImagemMemoriaPorNome (String nome) {
        return Files.exists(Path.of(pasta + nome));
    }

    public void deletarTodosMenosNomes(List<String> nomes) {
        File local = new File(pasta);
        if (local.exists() && local.isDirectory()) {
            File[] imagens = local.listFiles(); // Obtém todos os arquivos
            assert imagens != null;
            for (File a : Arrays.stream(imagens).toList()) {
                if (!nomes.contains(a.getName())) deletarImagemMemoria(a.getName());
            }
        }
    }




    public ResponseEntity<byte[]> imprimirPNG(String idNome){
        try {
            byte[] imagemBytes = Files.readAllBytes(getCaminho(idNome));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(imagemBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}


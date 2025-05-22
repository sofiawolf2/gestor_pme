package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.User;
import br.com.taurustech.gestor.model.dto.UserDTO;
import br.com.taurustech.gestor.repository.UserRepository;
import br.com.taurustech.gestor.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.taurustech.gestor.validator.ValidatorUtil.validarUUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator validator;
    private final RoleService roleService;

    String erroNotFound = "Usuário não encontrado";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private User pesquisarValidando(String id){
        return userRepository.findById(Objects.requireNonNull(validarUUID(id))).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }
    public User buscarUserAtual(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return buscarLogin(authentication.getName());
    }

    public User buscarUserID (String id){
        return pesquisarValidando(id);
    }
    public UserDTO buscarOutID(String id){ return UserDTO.createOutput(pesquisarValidando(id));}
    public User buscarLogin( String login){
        return userRepository.findByLogin(login);
    }
    public UUID getID(UserDTO dto){
        return userRepository.findByLogin(dto.getLogin()).getId();
    }

    public UserDTO cadastro(UserDTO dto){
        validator.validarNovoUser(dto);
        var user = dto.getUser(roleService);
        if (!dto.getSenha().isEmpty()&& dto.getSenha().length()>1) user.setSenha(encoder.encode(dto.getSenha()));
        return UserDTO.createOutput(userRepository.save(user));
    }

    public List<UserDTO> pesquisarTodos(String nome, String login, String email, String role) {
        var user = new User();
        user.setNome(nome);
        user.setLogin(login);
        user.setEmail(email);
        user.setRole(roleService.buscarNomeProx(role));
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<User> userExample = Example.of(user,matcher);
        var lista = userRepository.findAll(userExample);
        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(UserDTO::createOutput).toList();
    }
    public void deletarTodosMenosLogin (List<String> login ){
        userRepository.deleteAllExcept(login);
    }

    public void deletarStringId(String id) {
        var retorno = pesquisarValidando(id);
        userRepository.delete(retorno);
    }

    public void atualizarPatch(UserDTO dto, String id){
        var user = pesquisarValidando(id);
        validator.validarAtualizado(dto, user.getId());
        if (dto.getNome()!=null) user.setNome(dto.getNome());
        if (dto.getLogin()!=null) user.setLogin(dto.getLogin());
        if (dto.getEmail()!=null) user.setEmail(dto.getEmail());
        if (dto.getSenha()!=null) user.setSenha(encoder.encode(dto.getSenha()));
        if (dto.getRole()!=null) user.setRole(roleService.buscarNome(dto.getRole()));

        userRepository.save(user);
    }





}

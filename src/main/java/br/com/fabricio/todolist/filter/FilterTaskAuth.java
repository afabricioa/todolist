package br.com.fabricio.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fabricio.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{
    //todas as requisições vão passar primeiro por esse filtro

    @Autowired
    private IUserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
            var servletPath = request.getServletPath();
            System.out.println(servletPath);
            if(servletPath.startsWith("/tasks/")){
                //pega autorizacao
                var authorization = request.getHeader("Authorization");
                var authEncoded = authorization.substring("Basic".length()).trim(); //remove a palavra basic da string 'Basic ijdiwe#122'

                byte[] authDecoded = Base64.getDecoder().decode(authEncoded); //decodifica o base64 transformando num array de bytes
                var authStringDecoded = new String(authDecoded); //cria uma string com base no array de bytes
                
                String[] credentials = authStringDecoded.split(":");
                String username = credentials[0];
                String password = credentials[1];
                //valida usuario
                var user = this.userRepository.findByUsername(username);
                if(user == null){
                    response.sendError(401, "Usuário sem autorização ou não encontado!");
                }else{
                    //valida senha
                    var result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    if(result.verified){
                        //segue viagem
                        System.out.println("entrou");
                        request.setAttribute("idUser", user.getId());
                        filterChain.doFilter(request, response);
                    }else{
                        response.sendError(401, "Credenciais incorretas!");
                    }
                    
                }
            }else{
                filterChain.doFilter(request, response);
            }
                
    }
    
    
    
}

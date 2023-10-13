package br.com.leanfj.todolist.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.leanfj.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


                var servletPath = request.getServletPath();


                if(
                    servletPath.equals("/users/") ||
                    servletPath.equals("/h2-console")
                    ) {
                    filterChain.doFilter(request, response);
                    return;
                }

                var authorization = request.getHeader("Authorization");


                if (authorization == null || !authorization.startsWith("Basic")) {
                    response.sendError(401);
                    return;
                }


                var authorizationEncoded = authorization.substring("Basic".length()).trim();

                var authorizationDecoded = new String(java.util.Base64.getDecoder().decode(authorizationEncoded));


                String[] credentials = authorizationDecoded.split(":");

                String username = credentials[0];
                String password = credentials[1];

                var user = this.userRepository.findByUserName(username);

                if(user == null) {
                    response.sendError(401);
                    return;
                } else {

                    var passwordHashed = user.getPassword();
                    var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), passwordHashed);


                    if(!passwordVerified.verified) {
                        response.sendError(401);
                        return;
                    }

                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);

                }


    }
    
}

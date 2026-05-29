package com.petshop.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PetShop API")
                        .version("1.0.0")
                        .description("""
                                API REST para e-commerce de produtos para pets.
                                
                                ## Funcionalidades
                                - **Categorias**: Organização dos produtos por tipo (alimentação, higiene, brinquedos, etc.)
                                - **Produtos**: Catálogo completo com controle de estoque
                                - **Clientes**: Cadastro e gestão de clientes
                                - **Pedidos**: Criação e acompanhamento de pedidos com controle automático de estoque
                                
                                ## Status de Pedido
                                - `AGUARDANDO_PAGAMENTO` → `PAGO` → `EM_SEPARACAO` → `ENVIADO` → `ENTREGUE`
                                - Qualquer status pode ir para `CANCELADO` (exceto ENTREGUE)
                                """)
                        .contact(new Contact()
                                .name("PetShop Owner")
                                .email("contato@petshop.com.br"))
                        .license(new License()
                                .name("MIT License")));
    }
}

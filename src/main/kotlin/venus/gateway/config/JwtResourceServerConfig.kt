package venus.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableResourceServer
class JwtResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
    }

    override fun configure(config: ResourceServerSecurityConfigurer) {
        config.tokenServices(tokenServices())
    }

    @Bean
    fun tokenStore(): TokenStore = JwtTokenStore(accessTokenConverter())

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter =
            JwtAccessTokenConverter().apply { setSigningKey(PRIVATE_KEY) }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices =
            DefaultTokenServices().apply { setTokenStore(tokenStore()) }

    companion object {
        private const val PRIVATE_KEY = "auth_private_key"
    }
}

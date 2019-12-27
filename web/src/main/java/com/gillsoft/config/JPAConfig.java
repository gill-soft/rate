package com.gillsoft.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.gillsoft.repository")
public class JPAConfig {
	
	private static final String SSH_USE = "ssh.use";
	private static final String SSH_HOST = "ssh.host";
	private static final String SSH_PORT = "ssh.port";
	private static final String SSH_USER = "ssh.user";
	private static final String SSH_PASSWORD = "ssh.password";
	private static final String SSH_KEY = "ssh.private_key";
	private static final String SSH_LOCAL_HOST = "ssh.local.host";
	private static final String SSH_LOCAL_PORT = "ssh.local.port";
	private static final String SSH_REMOTE_PORT = "ssh.remote.port";

	@Autowired
	private Environment env;

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
		lcemfb.setDataSource(getDataSource());
		lcemfb.setPersistenceUnitName("rate");
		lcemfb.setJpaVendorAdapter(getJpaVendorAdapter());
		lcemfb.setJpaProperties(jpaProperties());
		return lcemfb;
	}

	@Bean
	public JpaVendorAdapter getJpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("database.driverClassName"));
		dataSource.setUrl(env.getProperty("database.url"));
		dataSource.setUsername(env.getProperty("database.username"));
		dataSource.setPassword(env.getProperty("database.password"));
		return dataSource;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager txManager() {
		if (isUseSsh()) {
			tunnel();
		}
		return new JpaTransactionManager(getEntityManagerFactoryBean().getObject());
	}

	private Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		properties.put("hibernate.id.new_generator_mappings", env.getProperty("hibernate.id.new_generator_mappings"));
		return properties;
	}
	
	private boolean isUseSsh() {
		return Boolean.valueOf(env.getProperty(SSH_USE));
	}
	
	private static Session session;
	
	@Scheduled(initialDelay = 15000, fixedDelay = 5000)
	public void tunnel() {
		if (isUseSsh()) {
			if (session == null) {
				session = newSession();
			}
			if (!session.isConnected()) {
				session.disconnect();
				session = newSession();
			}
		}
	}
	
	private Session newSession() {
		JSch jsch = new JSch();
		try {
			jsch.addIdentity(JPAConfig.class.getClassLoader().getResource(env.getProperty(SSH_KEY)).getPath(), getPassword());
			Session session = jsch.getSession(env.getProperty(SSH_USER),
					env.getProperty(SSH_HOST), Integer.valueOf(env.getProperty(SSH_PORT)));
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			session.setPortForwardingL(Integer.valueOf(env.getProperty(SSH_LOCAL_PORT)),
					env.getProperty(SSH_LOCAL_HOST), Integer.valueOf(env.getProperty(SSH_REMOTE_PORT)));
			return session;
		} catch (JSchException e) {
		}
		return null;
	}
	
	private byte[] getPassword() {
		String pass = env.getProperty(SSH_PASSWORD);
		if (pass == null
				|| pass.isEmpty()) {
			return null;
		} else {
			return pass.getBytes();
		}
	}
	
}

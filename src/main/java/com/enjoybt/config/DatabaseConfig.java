package com.enjoybt.config;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import org.apache.tomcat.jdbc.pool.DataSource;

@Configuration
@Lazy
@EnableTransactionManagement
public class DatabaseConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${mybatis.mapper-location}")
	private String mybatisMapperLocation;
	
	@Value("${mybatis.config-location}")
	private String mybatisConfigLocation;

	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryForMyBatis(DataSource dataSource, ApplicationContext applicationContext) throws IOException  {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(mybatisConfigLocation));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mybatisMapperLocation));
		
		return sqlSessionFactoryBean;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
	
	@Bean(destroyMethod="clearCache")
	public SqlSession sqlSessionForMyBatis(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
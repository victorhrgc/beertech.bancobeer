package beertech.becks.api.configurations;

import beertech.becks.api.model.converter.GeneralConverter;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Collections;

@Configuration
public class MongoDBConfig {

	@Autowired
	private MongoProperties mongoProperties;

	@Autowired
	private MongoClient mongoClient;

	@Bean
	public MongoTemplate mongoTemplate() {

		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, mongoProperties.getDatabase());
		MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
		mongoMapping.setCustomConversions(customConversions());
		mongoMapping.afterPropertiesSet();
		return mongoTemplate;

	}

	public MongoCustomConversions customConversions() {
		return new MongoCustomConversions(Collections.singletonList(new GeneralConverter.UserRolesConverter()));
	}
}
package dev.tehsteel.tbooks.user.database;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/* Test that mongodb's connection */
@DataMongoTest
@ExtendWith(SpringExtension.class)
public class MongoConnectionTest {
	private static final String COLLECTION_NAME = "testCollection";
	private static final String TEST_KEY = "test";
	private static final String TEST_VALUE = "worked!";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void testMongoConnection() {
		final DBObject testObject = BasicDBObjectBuilder
				.start()
				.add(TEST_KEY, TEST_VALUE)
				.get();


		mongoTemplate.save(testObject, COLLECTION_NAME);

		assertThat(mongoTemplate.findAll(DBObject.class, COLLECTION_NAME)).extracting(TEST_KEY)
				.containsOnly(TEST_VALUE);

		mongoTemplate.remove(testObject, COLLECTION_NAME);
	}
}

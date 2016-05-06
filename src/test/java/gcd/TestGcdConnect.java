package gcd;

import com.google.gcloud.AuthCredentials;
import com.google.gcloud.datastore.Datastore;
import com.google.gcloud.datastore.DatastoreOptions;
import com.google.gcloud.datastore.DateTime;
import com.google.gcloud.datastore.Entity;
import com.google.gcloud.datastore.Key;
import com.google.gcloud.datastore.KeyFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author thomas.haines.
 */
public class TestGcdConnect {

  @Test
  public void testConnect_includeScheme() throws IOException {
    testConnect_localhost(true);
  }

  @Test
  public void testConnect_noScheme() throws IOException {
    testConnect_localhost(false);
  }

  void testConnect_localhost(boolean includeScheme) throws IOException {
    String gcdIp = System.getProperty("gcp_ip");
    if (gcdIp == null) {
      gcdIp = "localhost";
    }


    String url = String.format("http://%s:8000", gcdIp);
    if (!includeScheme) {
      url = String.format("%s:8000", gcdIp);
    }

    System.out.println("Connecting to GCP url=" + url);

    Datastore datastore = DatastoreOptions.builder()
        .host(url)
        .projectId("sample-ds")
        .authCredentials(AuthCredentials.noAuth())
        .build().service();

    Assert.assertNotNull(datastore);

    KeyFactory keyFactory = datastore.newKeyFactory().kind("keyKind");
    Key key = keyFactory.newKey("keyName");
    Entity entity = Entity.builder(key)
        .set("name", "John Doe")
        .set("age", 30)
        .set("access_time", DateTime.now())
        .build();
    datastore.put(entity);

    System.out.println("datastore accessed successfully");
  }



}

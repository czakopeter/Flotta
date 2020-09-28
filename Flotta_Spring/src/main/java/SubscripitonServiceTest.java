import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.czp.service.SubscriptionService;


//@SpringBootTest
public class SubscripitonServiceTest {

  SubscriptionService subscriptionService;
  
  @Autowired
  public void setSubscriptionService(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }
  
  @Test
  public void something() {
    
  }
}

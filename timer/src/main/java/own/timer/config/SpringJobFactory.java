package own.timer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    @Autowired
    private transient AutowireCapableBeanFactory autowireCapableBeanFactory;

}

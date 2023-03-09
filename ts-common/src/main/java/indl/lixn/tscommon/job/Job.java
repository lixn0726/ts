package indl.lixn.tscommon.job;

import indl.lixn.tscommon.support.Id;

import java.util.Properties;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:11
 **/
public interface Job {

    Id getId();

    JobContent getContent();

    JobTrigger getTrigger();

    Properties getProperties();

    void setContent();

    void setTrigger();

    void addProperty(String key, String val);


    class TestingDefaultJob implements Job {

        private Properties props;

        @Override
        public Id getId() {
            return null;
        }

        @Override
        public JobContent getContent() {
            return null;
        }

        @Override
        public JobTrigger getTrigger() {
            return null;
        }

        @Override
        public Properties getProperties() {
            return null;
        }

        @Override
        public void setContent() {

        }

        @Override
        public void setTrigger() {

        }

        @Override
        public void addProperty(String key, String val) {

        }
    }
}

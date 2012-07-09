package jsp;

import org.hibernate.cfg.AnnotationConfiguration;

class ItemManagerFactory {
    
    static ItemManager instance;

    static ItemManager getManager() {
        if (instance == null)
            instance = new ItemManager(new AnnotationConfiguration().configure().buildSessionFactory());
        return instance;
    }
    
}

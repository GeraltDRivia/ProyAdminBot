package com.proyectofinalad.bot.repositories.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.proyectofinalad.bot.models.entities.Category;
import com.proyectofinalad.bot.models.entities.Client;
import com.proyectofinalad.bot.models.entities.Message;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.models.entities.OrderItem;
import com.proyectofinalad.bot.models.entities.OrderStatus;
import com.proyectofinalad.bot.models.entities.Product;

public final class HibernateSessionFactory {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateSessionFactory() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();

        new EnvironmentPropertiesPopulator().populate(configuration.getProperties());
        addAnnotatedClasses(configuration);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    private static void addAnnotatedClasses(Configuration configuration) {
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(OrderItem.class);
        configuration.addAnnotatedClass(OrderStatus.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(Message.class);
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

}

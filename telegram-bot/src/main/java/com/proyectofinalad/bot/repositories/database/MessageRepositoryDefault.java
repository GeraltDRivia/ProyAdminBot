package com.proyectofinalad.bot.repositories.database;

import static com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;

import com.proyectofinalad.bot.models.entities.Message;
import com.proyectofinalad.bot.repositories.MessageRepository;

public class MessageRepositoryDefault implements MessageRepository {

    @Override
    public Message findByName(String messageName) {
        String query = "from Message where name = :name";

        return inTransaction(session ->
                session.createQuery(query, Message.class)
                        .setParameter("name", messageName)
                        .setMaxResults(1)
                        .uniqueResult()
        );
    }

}

package com.proyectofinalad.bot.repositories.database;

import static com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;
import static com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory.inTransactionVoid;

import com.proyectofinalad.bot.models.entities.Client;
import com.proyectofinalad.bot.repositories.ClientRepository;

public class ClientRepositoryDefault implements ClientRepository {

    @Override
    public Client findByChatId(Long chatId) {
        String query = "from Client where chatId = :chatId";

        return inTransaction(session ->
                session.createQuery(query, Client.class)
                        .setParameter("chatId", chatId)
                        .setMaxResults(1)
                        .uniqueResult()
        );
    }

    @Override
    public void save(Client client) {
        inTransactionVoid(session -> session.persist(client));
    }

    @Override
    public void update(Client client) {
        inTransactionVoid(session -> session.merge(client));
    }

}

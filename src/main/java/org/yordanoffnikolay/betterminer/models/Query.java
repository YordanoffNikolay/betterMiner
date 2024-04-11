package org.yordanoffnikolay.betterminer.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "queries")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "server_query", columnDefinition = "TEXT")
    private String serverQuery;
    @Column(name = "client_url", columnDefinition = "TEXT")
    private String clientUrl;

    @ManyToOne
    @JoinColumn(name = "inn_id")
    private Inn inn;

    public Query() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerQuery() {
        return serverQuery;
    }

    public void setServerQuery(String serverQuery) {
        this.serverQuery = serverQuery;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public Inn getInn() {
        return inn;
    }

    public void setInn(Inn inn) {
        this.inn = inn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query queries = (Query) o;
        return Objects.equals(id, queries.id) &&
                Objects.equals(serverQuery, queries.serverQuery) &&
                Objects.equals(clientUrl, queries.clientUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverQuery, clientUrl);
    }
}

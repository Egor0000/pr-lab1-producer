package md.utm.isa.pr.lab1.producer.service;

import md.utm.isa.pr.lab1.producer.entity.Table;

public interface TableService{

    Table takeOccupiedTable() throws InterruptedException;

    void moveTableToWait(Table table);

    Table getById(Long id);

    void removeTableFromWait(int id);

    double getAverage(double next);

    int ratingToStar(double rating);
}

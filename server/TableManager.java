package server;

import java.util.ArrayList;

public class TableManager {
    private ArrayList<Table> tables;

    public TableManager(int numberOfTables) {
        tables = new ArrayList<>();
        for (int k = 0; k < numberOfTables; k++) {
            tables.add(new Table());
        }
    }

    public void addTable() {
        tables.add(new Table());
    }

    public void removeTable(int index) {
        tables.remove(index);
    }

    public int getNumberofTables() {
        return tables.size();
    }

    public Table getTable(int index) {
        return tables.get(index);
    }

    public void start(int index) {
        tables.get(index).start();
    }

    public void stop(int index) {
        tables.get(index).stop();
    }
}

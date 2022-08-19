package restaurant.core;

import restaurant.core.interfaces.Controller;
import restaurant.entities.drinks.Fresh;
import restaurant.entities.drinks.Smoothie;
import restaurant.entities.healthyFoods.Salad;
import restaurant.entities.healthyFoods.VeganBiscuits;
import restaurant.entities.healthyFoods.interfaces.HealthyFood;
import restaurant.entities.drinks.interfaces.Beverages;
import restaurant.entities.tables.InGarden;
import restaurant.entities.tables.Indoors;
import restaurant.entities.tables.interfaces.Table;
import restaurant.repositories.BeverageRepositoryImpl;;
import restaurant.repositories.HealthFoodRepositoryImpl;
import restaurant.repositories.TableRepositoryImpl;
import restaurant.repositories.interfaces.*;

import static restaurant.common.ExceptionMessages.*;
import static restaurant.common.OutputMessages.*;

public class ControllerImpl implements Controller {
    private HealthFoodRepository<HealthyFood> healthFoodRepository;
    private BeverageRepository<Beverages> beverageRepository;
    private TableRepository<Table> tableRepository;
    private double totalIncome;


    public ControllerImpl(HealthFoodRepository<HealthyFood> healthFoodRepository, BeverageRepository<Beverages> beverageRepository, TableRepository<Table> tableRepository) {
        this.healthFoodRepository = new HealthFoodRepositoryImpl();
        this.beverageRepository = new BeverageRepositoryImpl();
        this.tableRepository = new TableRepositoryImpl();
    }

    @Override
    public String addHealthyFood(String type, double price, String name) {
        HealthyFood healthyFood = null;
        if ("Salad".equals(type)) {
            healthyFood = new Salad(name, price);
        } else if ("Cake".equals(type)) {
            healthyFood = new VeganBiscuits(name, price);
        }
        if (this.healthFoodRepository.foodByName(name) != null) {
            throw new IllegalArgumentException(String.format(FOOD_EXIST, name));
        } else {

            this.healthFoodRepository.add(healthyFood);
            return String.format(FOOD_ADDED, name);
        }
    }
    @Override
    public String addBeverage(String type, int counter, String brand, String name){
        Beverages beverages = null;
        if ("Fresh".equals(type)) {
            beverages = new Fresh(name, counter, brand);
        } else if ("Smoothie".equals(type)) {
            beverages = new Smoothie(name, counter, brand);
        }

        if (this.beverageRepository.beverageByName(name, brand) != null) {
            throw new IllegalArgumentException(String.format(BEVERAGE_EXIST, name));
        } else {
            this.beverageRepository.add(beverages);
            return String.format(BEVERAGE_ADDED, type, brand);
        }

    }

    @Override
    public String addTable(String type, int tableNumber, int capacity) {
        Table table = null;
        if ("Indoors".equals(type)) {
            table = new Indoors(tableNumber, capacity);
        } else if ("InGarden".equals(type)) {
            table = new InGarden(tableNumber, capacity);
        }

        if (this.tableRepository.byNumber(tableNumber) != null) {
            throw new IllegalArgumentException(String.format(TABLE_IS_ALREADY_ADDED, tableNumber));
        } else {
            this.tableRepository.add(table);
            return String.format(TABLE_ADDED, tableNumber);
        }
    }


    @Override
    public String reserve(int numberOfPeople) {
        Table tableToReserve = this.tableRepository.getAllEntities().stream().filter(t -> t.getSize() >= numberOfPeople && !t.isReservedTable())
                .findFirst().orElse(null);
        if (tableToReserve == null) {
            return (String.format(RESERVATION_NOT_POSSIBLE, numberOfPeople));
        } else {
            tableToReserve.reserve(numberOfPeople);
            return String.format(TABLE_RESERVED, tableToReserve.getTableNumber(), numberOfPeople);
        }
    }

    @Override
    public String orderHealthyFood(int tableNumber, String healthyFoodName) {
        Table table = this.tableRepository.byNumber(tableNumber);
        if (table == null || !table.isReservedTable()) {
            return String.format(WRONG_TABLE_NUMBER, tableNumber);
        }
        HealthyFood healthyFood = this.healthFoodRepository.foodByName(healthyFoodName);
        if (healthyFood == null) {
            return String.format(NONE_EXISTENT_FOOD, healthyFoodName);
        }
        tableRepository.byNumber(tableNumber).orderHealthy(healthyFood);
        return String.format(FOOD_ORDER_SUCCESSFUL, healthyFoodName, tableNumber);
    }

    @Override
    public String orderBeverage(int tableNumber, String name, String brand) {
        Table table = this.tableRepository.byNumber(tableNumber);
        if (table == null || !table.isReservedTable()) {
            return String.format(WRONG_TABLE_NUMBER, tableNumber);
        }
        Beverages beverages = this.beverageRepository.beverageByName(name, brand);
        if (beverages == null) {
            return String.format(NON_EXISTENT_DRINK, name, brand);
        }
        table.orderBeverages(beverages);
        return String.format(BEVERAGE_ORDER_SUCCESSFUL, name, tableNumber);
    }

    @Override
    public String closedBill(int tableNumber) {
        Table table = this.tableRepository.byNumber(tableNumber);
        if (table == null) {
            throw new IllegalArgumentException(String.format(WRONG_TABLE_NUMBER, tableNumber));
        }
        double bill = table.bill();
        totalIncome += bill;
        table.clear();

        return String.format(BILL, tableNumber, bill);
    }


    @Override
    public String totalMoney() {
        return String.format(TOTAL_MONEY, totalIncome);
    }
    }

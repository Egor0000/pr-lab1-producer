# Producer (Dinning Hall) for Network Programming Lab. 1

### Dinning Hall
- [] Add editable time units
- [] Receives ready orders and puts to common list
- [] Notify the waiter about received order
#### Table
- [] Implement TableService logic. There are finite number of tables. nr should be configurable
  - [] Working on separate threads
  - [] Generate a random Order based on Restaurant menu when a waiter picks up the order
  - [] Add wait time for table occupation after it is reserved 
  - [] States of a table: FREE, IDLE, WAIT
  - [] When a ready order is received, it is checked for authenticity
### Waiter.
- A waiter takes orders from tables
- Nr of waiters is less than nr of tables. Should be configurable
- [] Wait time to take the order
- [] Working on separate threads
- [] Looks for FREE table
- [] Send the order to Kitchen
- [] Serve order to table
### Restaurant Menu
- Consists of food
### Order
- [] Should contain 
  - [] unique order id 
  - [] >0 menu items ids
  - [] priority in range (1, 5)
  - [] max wait time.
    - [] take item with the highest separation time from the order multiplied by 1.3
  - [] the time an order was picked up by waiter

### Reputation System
- [] After an order is saved, it is checked the time taken to prepare and deliver it
```
Order_total_preparing_time = order_pick_up_timestamp - order_serving_timestamp
```
- [] Develop reputation system
- [] Calculate mark based on reputation system
- [] Calculate average after each order was served 

## Checkpoint 1 (Producer)
- [x] Repository
- [] README.md
- [x] webserver
- [x] docker
- [] Communication with Consumer
  - [x] Send POST request
  - [] Receive ready orders 
  - [] Logs for receiving
  - [] Logs for sending
- [x] Table logic (basic)
  - [x] Add threads
  - [x] Generate random orders
# orders-service

![CI/CD](https://github.com/ProgramowanieZespoloweIS2021/orders-service/actions/workflows/ci.yml/badge.svg)

[![codecov](https://codecov.io/gh/ProgramowanieZespoloweIS2021/orders-service/branch/main/graph/badge.svg?token=O0MYevLF8p)](https://codecov.io/gh/ProgramowanieZespoloweIS2021/orders-service)

## Possible env variables
- `POSTGRES_USER` (postgres)
- `POSTGRES_PASSWORD` (postgres)
- `POSTGRES_HOST` (eszopdb)
- `POSTGRES_PORT` (5432)
- `DATABASE` (postgres) - database name
- `SERVICE_PORT` (80)
- `OFFERS_HOST` (offers)
- `OFFERS_PORT` (80)

# Endpoints

### Get Order

`GET http://<hostname>/orders/{id}`

Returns:

```json
{
    "offer": {
    //        <values returned by offers/{id}
    },
    "buyer": {
      "firstName": "test2",
      "surname": "test2",
      "email": "test2"
    },
    "seller": {
      "firstName": "test1",
      "surname": "test1",
      "email": "test1"
    },
    "selectedTierId": 2,
    "id": 2,
    "creationDate": "2021-05-10-10-18-53",
    "description": "description2",
    "state": "ORDERED"
}
```

### Get Orders

`GET http://<hostname>/orders?<fitlering>&<sorting>&<pagination>`

Filtering options:

- DEFAULT: `none`
- `tier_id=<id>`
- `buyer_id=<id>`
- `offer_id=<id>`
- `state=<ORDERED|IN_PROGRESS|FINISHED>`
- `creation_date=<le|lt|ge|gt|eq>:<yyyy-MM-dd|yyyy-MM-dd-HH-mm-ss>`

Ordering options:

- DEFAULT: `order_by=desc:creation_date`
- `order_by=<asc|desc>:<field_name>`

Pagination options:

- DEFAULT: `limit=10&offset=0`
- `limit=<value>`
- `offset=<value>`

Returns:

```json
[
  {
    "offer": {
//        <values returned by offers/{id}
    },
    "buyer": {
      "firstName": "test2",
      "surname": "test2",
      "email": "test2"
    },
    "seller": {
      "firstName": "test1",
      "surname": "test1",
      "email": "test1"
    },
    "selectedTierId": 2,
    "id": 2,
    "creationDate": "2021-05-10-10-18-53",
    "description": "description2",
    "state": "ORDERED"
  }...
]
```

### Create Order

`POST http://<hostname>/orders`

Request Body:

```json
[
  {
  "buyerId": <id>,
  "offerId":  <id>,
  "tierId": <id>,
  "description":  "desc"
  }...
]
```

Returns:

```json
{
  "message": "Created"
}
```

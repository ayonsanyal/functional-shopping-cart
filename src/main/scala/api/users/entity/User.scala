package api.users.entity

case class User(name: String, email: String, address: Address)

case class Address(street: String, houseNumber: Int, postalCode: Int, country: String)

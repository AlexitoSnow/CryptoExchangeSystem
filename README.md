# Crypto Exchange System

## Description

CryptoExchangeSystem is a console project developed in Java that
aims to simulate the cryptocurrency market to exchange and buy
cryptocurrencies with your local currency.

## Table of Contents
- [Info](#info)
- [Features](#features)
- [Design Patterns](#design-patterns)
- [Contact](#contact)

## Info

- Java version: 17
- Project Type: Maven
- UI: Command console
- Architecture: MVC
- Data persistence: No

## Features

- User Authentication: Email and password
- Supports BTC and ETH
- Wallet Administration: Checks fiat money and cryptocurrencies
- Market: Place selling / buying orders or buy from the exchange
- Transactions Recording: Stores the successfully selling / buying orders and exchange transactions
- CryptoCurrency Fluctuation: The market fluctuates the cryptocurrencies prices in the background
- Market Order matching: The matching between selling and buying orders runs in the background

## Design Patterns

- Observer Pattern
The observer pattern is applied to notify controllers about the fluctuation in cryptocurrencies and about whether a match was found in buy and sell orders
- Singleton Pattern
  The singleton pattern is used in all classes where a single instance is needed, mainly in Service classes.
- Strategy Pattern
  The strategy pattern is present in the navigation of the project, implemented in the Router, which calls the controllers (the strategies).

## Contact

Alexander Nieves - alexitosnow@gmail.com
import React, { Component } from 'react';
import axios from 'axios';
import { Button, Input, message } from 'antd';

export default class Main extends Component {
    constructor(props) {
        super(props);
        this.state = {
            balance: null,
            inputDepositValue: '',
            inputWithdrawValue: '',
        };
    }

    logout = () => {
        axios.defaults.withCredentials = true;
        axios
            .get('http://localhost:8080/logout')
            .then((response) => {
                console.log(response.data);
                localStorage.removeItem('user');
                window.location.reload();
            })
            .catch((error) => {
                console.log(error);
            });
    };

    getDeposit = () => {
        axios.defaults.withCredentials = true;
        axios
            .get('http://localhost:8080/check')
            .then((response) => {
                console.log(response.data);
                this.setState({
                    balance: response.data.balance.toFixed(2),
                });
            })
            .catch((error) => {
                console.log(error);
            });
    };

    withdraw = () => {

        const amount = Number(this.state.inputWithdrawValue).toFixed(2);

        axios
            .post('http://localhost:8080/withdraw', {
                username: localStorage.getItem('user'),
                amount: amount
            })
            .then((response) => {
                this.setState({
                    balance: response.data.balance.toFixed(2),
                    inputWithdrawValue: '',
                });
                message.success('Withdraw successfully!');
            })
            .catch((error) => {
                if (error.response.status === 500){
                    console.log(error.response.status)
                    message.error(error.response.data.trace)}
                else{message.error(error.response.data);}
                console.log(this.state.username + this.state.inputWithdrawValue);
            });
    };

    deposit = () => {
        const amount = Number(this.state.inputDepositValue).toFixed(2);
        axios
            .post('http://localhost:8080/deposit', {
                username: localStorage.getItem('user'),
                amount: amount
            })
            .then((response) => {
                this.setState({
                    
                    balance: response.data.balance.toFixed(2),
                    inputDepositValue: '',
                });
                message.success('Deposit successfully!');
            })
            .catch((error) => {
                if (error.response.status === 500){
                    console.log(error.response.status)
                    message.error(error.response.data.trace)}
                else{message.error(error.response.data);}
            });
    };

    render() {
        return (
            <div className="form-container">
            <div className='main-container'>
                <h1>Hello {localStorage.getItem('user')}</h1>
                <Button type="primary" onClick={this.logout}>
                    Log out
                </Button>

                <div className="check-balance">
                    <h1>{this.state.balance}</h1>
                    <Button onClick={this.getDeposit}>Check your balance!</Button>
                </div>

                <div className="withdraw-balance">
                    <Input
                        type="text"
                        value={this.state.inputWithdrawValue}
                        onChange={(event) => {
                            let value = event.target.value;
                            const match = value.match(/^\d*(\.\d{0,2})?$/); // Match any number of digits and up to 2 decimal points
                            if (match) {
                                value = match[0]; // Use the matched value to allow typing the decimal point
                                this.setState({ inputWithdrawValue: event.target.value });
                            }
                           
                        }}
                        placeholder="Amount"
                    />
                    <Button onClick={this.withdraw}>Withdraw</Button>
                </div>

                <div className="deposit-balance">
                    <Input
                        type="text"
                        value={this.state.inputDepositValue}
                        onChange={(event) => {
                            let value = event.target.value;
                            const match = value.match(/^\d*(\.\d{0,2})?$/); // Match any number of digits and up to 2 decimal points
                            if (match) {
                                value = match[0]; // Use the matched value to allow typing the decimal point
                                this.setState({ inputDepositValue: event.target.value });
                            }
                        }}
                        placeholder="Amount"
                    />
                    <Button onClick={this.deposit}>Deposit</Button>
                </div>
                </div>
            </div>
        );
    }
}

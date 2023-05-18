import React, { Component } from 'react';
import axios from 'axios';
import { Form, Input, Button, Alert, message } from 'antd';
import {DollarOutlined, LockOutlined, UserOutlined} from "@ant-design/icons";

export default class SignUp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: null,
            password: null,
            balance: '',
        };
    }

    signUp = () => {
        console.log(this.state.username + this.state.password + this.state.balance);
        axios.defaults.withCredentials = true;

        // Format the balance to always have 2 decimal places
        let balance = Number(this.state.balance).toFixed(2);

        axios
            .post('http://localhost:8080/register', {
                username: this.state.username,
                password: this.state.password,
                balance: balance,
            })
            .then((response) => {
                console.log(response.data);
                localStorage.setItem('user', this.state.username);
                window.location.reload();
            })
            .catch((error) => {
                console.log(error);
                message.error(error.response.data)
            });
    };

    handleBalanceChange = (event) => {
        let value = event.target.value;
        const match = value.match(/^\d*(\.\d{0,2})?$/); // Match any number of digits and up to 2 decimal points
        if (match) {
            value = match[0]; // Use the matched value to allow typing the decimal point
            this.setState({ balance: value });
        }
    };


    render() {
        return (
            <div className="form-container">
                <Form>
                    <Form.Item label="Username">
                        <Input
                            prefix={<UserOutlined className="site-form-item-icon" />}
                            type="text"
                            onChange={(event) => {
                                this.setState({ username: event.target.value });
                            }}
                            placeholder="Username"
                        />
                    </Form.Item>
                    <Form.Item label="Password">
                        <Input.Password
                            prefix={<LockOutlined className="site-form-item-icon" />}
                            type="password"
                            onChange={(event) => {
                                this.setState({ password: event.target.value });
                            }}
                            placeholder="Password"
                        />
                    </Form.Item>
                    <Form.Item label="Balance">
                        <Input
                            prefix={<DollarOutlined className="site-form-item-icon" />}
                            type="text"
                            onChange={this.handleBalanceChange}
                            value={this.state.balance}
                            placeholder="Initial Balance"
                        />
                    </Form.Item>

                    <br/>

                    <Form.Item>
                        <Button type="primary" onClick={this.signUp}>
                            Sign up
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}
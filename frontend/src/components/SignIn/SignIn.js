import React, { Component } from 'react';
import './SignIn.css';
import axios from 'axios';
import {Form, Input, Button, Alert, message} from 'antd';
import {LockOutlined, UserOutlined} from "@ant-design/icons";

export default class SignIn extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        };
    }

    login = () => {
        console.log(this.state.username + this.state.password);
        axios.defaults.withCredentials = true;
        axios
            .get("http://localhost:8080/login?"+`username=${this.state.username}&`+`password=${this.state.password}`)
            .then((response) => {
                console.log(response.data);
                localStorage.setItem('user', this.state.username);
                window.location.reload();

                if (this.state.loginFail) {
                    message.success('Login successfully!');
                }
            })
            .catch((error) => {
                message.error('Login unsuccessfully!');
            });
    };

    render() {
        return (
            <div className="form-container">
                <Form>
                    <Form.Item label="Username">
                        <Input
                            prefix={<UserOutlined className="site-form-item-icon" />}
                            type="text"
                            placeholder="Username"
                            onChange={(event) => {
                                this.setState({ username: event.target.value });
                            }}
                        />
                    </Form.Item>

                    <Form.Item label="Password">
                        <Input.Password
                            prefix={<LockOutlined className="site-form-item-icon" />}
                            placeholder="Password"
                            onChange={(event) => {
                                this.setState({ password: event.target.value });
                            }}
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" onClick={this.login}>
                            Log In
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

import React,{Component} from 'react';
import SignIn from '../SignIn/SignIn';
import SignUp from '../SignUp/SignUp';
import './Login.css';
import money from '../../image/money.png'

class Login extends Component{
    constructor(props){
        super(props);
        this.state = {
            isLogin: true
        }
    }

    changeLogin = ()=>{
        if (this.state.isLogin){
            this.setState({
                isLogin:false
            });
        }
        else{
            this.setState({
                isLogin:true
            });
        }
    }

    render(){
        return(
            <div className="container">
                <div className='loginPage_main'>
                    <div>
                        <img src={money} width="300px"  alt="Money Bank Logo" />
                        <h1>Money Bank</h1>
                    </div>

                    <div className='loginPage_component'>
                        { this.state.isLogin ?
                            <SignIn/>:
                            <SignUp/>
                        }
                    </div>

                    <div className='signupOption'>
                        {
                            this.state.isLogin ?
                                <div>
                                    Don't have an account? <span onClick={this.changeLogin} style={{ "fontWeight":"bold", "color":"#0395F6"}}>Sign up</span>
                                </div>
                                :
                                <div>
                                    Have an account? <span onClick={this.changeLogin}  style={{ "fontWeight":"bold", "color":"#0395F6"}}>Sign in</span>
                                </div>
                        }
                    </div>
                </div>
            </div>
        );
    }
}

export default Login;
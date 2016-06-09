package ua.artcode.taxi.run;

import ua.artcode.taxi.remote.RemoteUserService;
import ua.artcode.taxi.view.UserLogin;

import java.io.IOException;

public class RunLogin {
    public static void main(String[] args) throws IOException {

        new UserLogin(new RemoteUserService());

    }
}

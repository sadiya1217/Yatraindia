import { createContext, useContext, useState } from "react";
import API_BASE_URL from "../services/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [token, setToken] = useState(
        localStorage.getItem("yatraindia_token")
    );

    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem("yatraindia_user");

        return savedUser
            ? JSON.parse(savedUser)
            : null;
    });

    const login = async (email, password) => {
        const response = await fetch(
            `${API_BASE_URL}/api/auth/login`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    password,
                }),
            }
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(
                data.message || "Login failed"
            );
        }

        localStorage.setItem(
            "yatraindia_token",
            data.token
        );

        const loggedInUser = {
            userId: data.userId,
            email: data.email,
            role: data.role,
        };

        localStorage.setItem(
            "yatraindia_user",
            JSON.stringify(loggedInUser)
        );

        setToken(data.token);
        setUser(loggedInUser);

        return data;
    };

    const logout = () => {
        localStorage.removeItem("yatraindia_token");
        localStorage.removeItem("yatraindia_user");

        setToken(null);
        setUser(null);
    };

    return (
        <AuthContext.Provider
            value={{
                token,
                user,
                login,
                logout,
                isAuthenticated: Boolean(token),
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
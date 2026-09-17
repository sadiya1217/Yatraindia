import API_BASE_URL from "./api.js";

const apiClient = async (endpoint, options = {}) => {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {}),
        },
        ...options,
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `API request failed: ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
};

export default apiClient;
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import api from "../api/client";
import Message from "../components/Message";

export default function LoginPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [message, setMessage] = useState("");

  async function submit(event) {
    event.preventDefault();
    setMessage("");
    try {
      const response = await api.post("/auth/login", form);
      const data = response.data.data;
      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);
      localStorage.setItem("fullName", data.fullName);
      navigate("/dashboard");
    } catch (error) {
      setMessage(error.response?.data?.message || "Login failed");
    }
  }

  return (
    <div className="grid min-h-screen place-items-center px-4">
      <form className="panel w-full max-w-md" onSubmit={submit}>
        <h1 className="text-2xl font-semibold">Login</h1>
        <p className="mt-1 text-sm text-slate-500">Access your banking dashboard.</p>
        <div className="mt-5 space-y-3">
          <Message message={message} type="error" />
          <input className="field" type="email" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          <input className="field" type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          <button className="btn w-full">Login</button>
        </div>
        <p className="mt-4 text-center text-sm">
          New user? <Link className="text-blue-700" to="/register">Create account</Link>
        </p>
      </form>
    </div>
  );
}

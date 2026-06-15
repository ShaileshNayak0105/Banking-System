import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import api from "../api/client";
import Message from "../components/Message";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: "", email: "", password: "", phone: "" });
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  async function submit(event) {
    event.preventDefault();
    setMessage("");
    try {
      await api.post("/auth/register", form);
      setType("info");
      setMessage("Registration successful. Please login.");
      setTimeout(() => navigate("/login"), 800);
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Registration failed");
    }
  }

  return (
    <div className="grid min-h-screen place-items-center px-4">
      <form className="panel w-full max-w-md" onSubmit={submit}>
        <h1 className="text-2xl font-semibold">Register</h1>
        <p className="mt-1 text-sm text-slate-500">Create a customer account.</p>
        <div className="mt-5 space-y-3">
          <Message message={message} type={type} />
          <input className="field" placeholder="Full name" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
          <input className="field" type="email" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          <input className="field" type="password" placeholder="Password" minLength="8" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          <input className="field" placeholder="Phone" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} required />
          <button className="btn w-full">Register</button>
        </div>
        <p className="mt-4 text-center text-sm">
          Already registered? <Link className="text-blue-700" to="/login">Login</Link>
        </p>
      </form>
    </div>
  );
}

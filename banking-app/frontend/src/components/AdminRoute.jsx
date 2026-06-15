import { Navigate, Outlet } from "react-router-dom";

export default function AdminRoute() {
  return localStorage.getItem("role") === "ADMIN" ? <Outlet /> : <Navigate to="/dashboard" replace />;
}

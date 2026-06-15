import { useEffect, useState } from "react";
import api from "../api/client";

function formatAmount(value) {
  const number = Number(value ?? 0);
  return number.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    async function load() {
      const response = await api.get("/admin/users");
      setUsers(response.data.data);
    }

    load();
  }, []);

  async function setStatus(accountNumber, action) {
    await api.put(`/admin/account/${accountNumber}/${action}`);
    const response = await api.get("/admin/users");
    setUsers(response.data.data);
  }

  return (
    <div className="space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Admin Users</h1>
        <p className="text-sm text-slate-500">Users with their linked account details</p>
      </div>

      <div className="panel overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-slate-50">
              <th className="table-cell">Name</th>
              <th className="table-cell">Email</th>
              <th className="table-cell">Account</th>
              <th className="table-cell">Balance</th>
              <th className="table-cell">Status</th>
              <th className="table-cell">Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td className="table-cell">{user.fullName}</td>
                <td className="table-cell">{user.email}</td>
                <td className="table-cell">{user.accountNumber || "-"}</td>
                <td className="table-cell">{user.balance == null ? "-" : `₹${formatAmount(user.balance)}`}</td>
                <td className="table-cell">{user.status || "-"}</td>
                <td className="table-cell">
                  {user.accountNumber && user.status === "ACTIVE" && (
                    <button className="btn-secondary" type="button" onClick={() => setStatus(user.accountNumber, "block")}>
                      Block
                    </button>
                  )}
                  {user.accountNumber && user.status === "BLOCKED" && (
                    <button className="btn-secondary" type="button" onClick={() => setStatus(user.accountNumber, "unblock")}>
                      Unblock
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

import { useEffect, useState } from "react";
import api from "../api/client";

function formatAmount(value) {
  const number = Number(value ?? 0);
  return number.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function statusClass(status) {
  return status === "BLOCKED"
    ? "bg-red-100 text-red-700"
    : "bg-green-100 text-green-700";
}

export default function DashboardPage() {
  const [details, setDetails] = useState(null);
  const [balance, setBalance] = useState(null);
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    async function load() {
      const [accountResponse, balanceResponse, historyResponse] = await Promise.all([
        api.get("/account/details"),
        api.get("/account/balance"),
        api.get("/transactions/history")
      ]);

      setDetails(accountResponse.data.data);
      setBalance(balanceResponse.data.data.balance);
      setTransactions(historyResponse.data.data.slice(0, 5));
    }

    load();
  }, []);

  return (
    <div className="space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Dashboard</h1>
        <p className="text-sm text-slate-500">Your current account snapshot</p>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <div className="panel">
          <p className="text-sm text-slate-500">Account Number</p>
          <p className="mt-2 text-xl font-semibold">{details?.accountNumber || "-"}</p>
          <p className="mt-1 text-sm text-slate-500">{details?.fullName}</p>
        </div>
        <div className="panel">
          <p className="text-sm text-slate-500">Balance</p>
          <p className="mt-2 text-4xl font-semibold">₹{formatAmount(balance)}</p>
        </div>
        <div className="panel">
          <p className="text-sm text-slate-500">Status</p>
          <span className={`mt-2 inline-flex rounded-full px-3 py-1 text-sm font-medium ${statusClass(details?.accountStatus)}`}>
            {details?.accountStatus || "-"}
          </span>
        </div>
      </div>

      <div className="panel overflow-x-auto">
        <h2 className="mb-3 text-lg font-semibold">Last 5 Transactions</h2>
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-slate-50">
              <th className="table-cell">Date</th>
              <th className="table-cell">Type</th>
              <th className="table-cell">Amount</th>
              <th className="table-cell">Description</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td className="table-cell">{new Date(transaction.createdAt).toLocaleString()}</td>
                <td className="table-cell">{transaction.type}</td>
                <td className="table-cell">₹{formatAmount(transaction.amount)}</td>
                <td className="table-cell">{transaction.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

import { useEffect, useState } from "react";
import api from "../api/client";

export default function AdminAuditLogs() {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    async function load() {
      const response = await api.get("/admin/audit-logs");
      setLogs(response.data.data);
    }

    load();
  }, []);

  return (
    <div className="space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Audit Logs</h1>
        <p className="text-sm text-slate-500">Chronological activity trail</p>
      </div>

      <div className="panel overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-slate-50">
              <th className="table-cell">Date</th>
              <th className="table-cell">User ID</th>
              <th className="table-cell">Action</th>
              <th className="table-cell">Details</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((log) => (
              <tr key={log.id}>
                <td className="table-cell">{new Date(log.createdAt).toLocaleString()}</td>
                <td className="table-cell">{log.userId}</td>
                <td className="table-cell">{log.action}</td>
                <td className="table-cell">{log.details}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

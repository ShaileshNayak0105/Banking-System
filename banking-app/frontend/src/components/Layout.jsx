import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";

const customerLinks = [
  ["/dashboard", "Dashboard"],
  ["/transfer", "Transfer"],
  ["/transactions", "Transactions"],
  ["/beneficiaries", "Beneficiaries"],
  ["/profile", "Profile"]
];

export default function Layout() {
  const navigate = useNavigate();
  const fullName = localStorage.getItem("fullName") || "User";

  function logout() {
    localStorage.clear();
    navigate("/login");
  }

  return (
    <div className="min-h-screen md:flex">
      <aside className="bg-slate-950 p-4 text-white md:min-h-screen md:w-64">
        <Link to="/dashboard" className="block text-lg font-semibold">
          Banking System
        </Link>
        <nav className="mt-6 grid gap-1">
          {customerLinks.map(([to, label]) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) =>
                `rounded-md px-3 py-2 text-sm ${isActive ? "bg-blue-700" : "text-slate-300 hover:bg-slate-800"}`
              }
            >
              {label}
            </NavLink>
          ))}
        </nav>
      </aside>
      <main className="flex-1">
        <header className="flex items-center justify-between border-b border-slate-200 bg-white px-5 py-3">
          <div>
            <p className="text-sm text-slate-500">Signed in as</p>
            <p className="font-medium">{fullName}</p>
          </div>
          <button className="btn-secondary" onClick={logout}>Logout</button>
        </header>
        <section className="p-5">
          <Outlet />
        </section>
      </main>
    </div>
  );
}

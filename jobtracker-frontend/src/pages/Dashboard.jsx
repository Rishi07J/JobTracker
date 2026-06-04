import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Dashboard.css";

const STATUS_OPTIONS = ["Applied", "Assessment", "Interview", "Offer", "Rejected"];

const STATUS_CLASSES = {
  Applied:    "status-applied",
  Assessment: "status-assessment",
  Interview:  "status-interview",
  Offer:      "status-offer",
  Rejected:   "status-rejected",
};

const STATUS_ICONS = {
  Applied:    "✦",
  Assessment: "◈",
  Interview:  "◎",
  Offer:      "★",
  Rejected:   "✕",
};

function StatCard({ label, value, variant }) {
  return (
    <div className={`stat-card stat-card--${variant}`}>
      <span className="stat-card__value">{value}</span>
      <span className="stat-card__label">{label}</span>
    </div>
  );
}

function StatusSelect({ value, onChange, className }) {
  return (
    <select
      className={`form-select status-select status-select--${(value || "").toLowerCase()} ${className || ""}`}
      value={value}
      onChange={onChange}
    >
      {STATUS_OPTIONS.map((s) => (
        <option key={s} value={s}>{STATUS_ICONS[s]} {s}</option>
      ))}
    </select>
  );
}

function Dashboard() {
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [resumeNames, setResumeNames] = useState({});
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({ company: "", role: "", status: "Applied" });

  useEffect(() => { fetchJobs(); }, []);

  const fetchJobs = async () => {
    setLoading(true);
    try {
      const response = await api.get("/jobs");
      setJobs(response.data);
      loadResumeNames(response.data);
    } catch (error) {
      console.log(error);
      setMessage("Failed to load jobs");
    } finally {
      setLoading(false);
    }
  };

  const loadResumeNames = async (jobsData) => {
    const resumeMap = {};
    for (const job of jobsData) {
      try {
        const response = await api.get(`/jobs/${job.id}/resume`);
        resumeMap[job.id] = response.data.filename;
      } catch {
        resumeMap[job.id] = null;
      }
    }
    setResumeNames(resumeMap);
  };

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.company.trim() || !formData.role.trim()) return;
    try {
      await api.post("/jobs", formData);
      setFormData({ company: "", role: "", status: "Applied" });
      fetchJobs();
    } catch (error) {
      console.log(error);
      setMessage("Failed to create job");
    }
  };

  const deleteJob = async (id) => {
    if (!window.confirm("Delete this job?")) return;
    try {
      await api.delete(`/jobs/${id}`);
      fetchJobs();
    } catch (error) {
      console.log(error);
      setMessage("Failed to delete job");
    }
  };

  const updateStatus = async (job, newStatus) => {
    try {
      await api.put(`/jobs/${job.id}`, {
        company: job.company,
        role: job.role,
        status: newStatus,
      });
      fetchJobs();
    } catch (error) {
      console.log(error);
      setMessage("Failed to update status");
    }
  };

  const uploadResume = async (jobId, file) => {
    if (!file) return;
    try {
      const resumeData = new FormData();
      resumeData.append("file", file);
      await api.post(`/jobs/${jobId}/resume`, resumeData);
      fetchJobs();
    } catch (error) {
      console.log(error);
      setMessage("Resume upload failed");
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  // Derived stats
  const stats = {
    total:      jobs.length,
    interviews: jobs.filter((j) => j.status === "Interview").length,
    offers:     jobs.filter((j) => j.status === "Offer").length,
    pending:    jobs.filter((j) => ["Applied", "Assessment"].includes(j.status)).length,
    rejected:   jobs.filter((j) => j.status === "Rejected").length,
  };

  return (
    <div className="dashboard">

      {/* ── Top bar ── */}
      <header className="topbar">
        <div className="topbar__brand">
          <div className="topbar__logo" aria-hidden="true">◈</div>
          <h1 className="topbar__title">Job Tracker</h1>
        </div>
        <button className="btn btn--logout" onClick={logout}>
          Logout
        </button>
      </header>

      <main className="dashboard__content">

        {/* ── Stats strip ── */}
        <div className="stats-grid" role="region" aria-label="Summary statistics">
          <StatCard label="Total jobs"  value={stats.total}      variant="purple" />
          <StatCard label="Interviews"  value={stats.interviews} variant="teal"   />
          <StatCard label="Offers"      value={stats.offers}     variant="green"  />
          <StatCard label="Pending"     value={stats.pending}    variant="amber"  />
          <StatCard label="Rejected"    value={stats.rejected}   variant="coral"  />
        </div>

        {/* ── Error message ── */}
        {message && (
          <div className="alert alert--danger" role="alert">
            <span className="alert__icon" aria-hidden="true">!</span>
            {message}
            <button
              className="alert__close"
              onClick={() => setMessage("")}
              aria-label="Dismiss"
            >✕</button>
          </div>
        )}

        {/* ── Add job form ── */}
        <section className="add-card" aria-label="Add a new job">
          <h2 className="add-card__title">
            <span className="add-card__title-icon" aria-hidden="true">+</span>
            Add new job
          </h2>
          <form className="add-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label" htmlFor="company">Company</label>
              <input
                id="company"
                type="text"
                name="company"
                placeholder="e.g. Anthropic"
                value={formData.company}
                onChange={handleChange}
                className="form-control"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="role">Role</label>
              <input
                id="role"
                type="text"
                name="role"
                placeholder="e.g. Frontend Engineer"
                value={formData.role}
                onChange={handleChange}
                className="form-control"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="status">Status</label>
              <StatusSelect
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
              />
            </div>
            <div className="form-group form-group--submit">
              <button type="submit" className="btn btn--primary">
                Add job
              </button>
            </div>
          </form>
        </section>

        {/* ── Jobs table ── */}
        <section className="table-section" aria-label="Job applications">
          {loading ? (
            <div className="table-empty">Loading jobs…</div>
          ) : jobs.length === 0 ? (
            <div className="table-empty">
              <span className="table-empty__icon" aria-hidden="true">◈</span>
              <p>No jobs yet. Add your first application above.</p>
            </div>
          ) : (
            <div className="table-wrap">
              <table className="jobs-table">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Company</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Resume</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {jobs.map((job) => (
                    <tr
                      key={job.id}
                      className={`jobs-table__row ${STATUS_CLASSES[job.status] || ""}`}
                    >
                      <td className="jobs-table__id">{job.id}</td>

                      <td className="jobs-table__company">{job.company}</td>

                      <td className="jobs-table__role">{job.role}</td>

                      <td className="jobs-table__status">
                        <StatusSelect
                          value={job.status}
                          onChange={(e) => updateStatus(job, e.target.value)}
                        />
                      </td>

                      <td className="jobs-table__resume">
                        {resumeNames[job.id] ? (
                          <>
                            <a
                              href={`http://localhost:8080/jobs/${job.id}/resume/download`}
                              target="_blank"
                              rel="noreferrer"
                              className="btn btn--resume-view"
                              title={resumeNames[job.id]}
                            >
                              ↗ {resumeNames[job.id]}
                            </a>
                            <label className="btn btn--upload" title="Replace resume">
                              ↑ Replace
                              <input
                                type="file"
                                accept=".pdf"
                                className="file-input--hidden"
                                onChange={(e) => uploadResume(job.id, e.target.files[0])}
                              />
                            </label>
                          </>
                        ) : (
                          <label className="btn btn--upload btn--upload-empty">
                            ↑ Upload PDF
                            <input
                              type="file"
                              accept=".pdf"
                              className="file-input--hidden"
                              onChange={(e) => uploadResume(job.id, e.target.files[0])}
                            />
                          </label>
                        )}
                      </td>

                      <td className="jobs-table__actions">
                        <button
                          className="btn btn--delete"
                          onClick={() => deleteJob(job.id)}
                          aria-label={`Delete ${job.company} – ${job.role}`}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default Dashboard;
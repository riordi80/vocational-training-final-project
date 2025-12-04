const Alert = ({
  type = "info",
  message,
  onClose,
  dismissible = true
}) => {
  const types = {
    success: {
      bg: "bg-green-50",
      border: "border-green-400",
      text: "text-green-800",
      icon: "✓"
    },
    error: {
      bg: "bg-red-50",
      border: "border-red-400",
      text: "text-red-800",
      icon: "✕"
    },
    warning: {
      bg: "bg-yellow-50",
      border: "border-yellow-400",
      text: "text-yellow-800",
      icon: "⚠"
    },
    info: {
      bg: "bg-blue-50",
      border: "border-blue-400",
      text: "text-blue-800",
      icon: "ℹ"
    }
  };

  const alertStyle = types[type];

  if (!message) return null;

  return (
    <div
      className={`${alertStyle.bg} border ${alertStyle.border} ${alertStyle.text} px-4 py-3 rounded relative flex items-center justify-between`}
      role="alert"
    >
      <div className="flex items-center">
        <span className="text-xl mr-3">{alertStyle.icon}</span>
        <span className="block sm:inline">{message}</span>
      </div>

      {dismissible && onClose && (
        <button
          onClick={onClose}
          className={`${alertStyle.text} hover:opacity-70 font-bold text-xl ml-4`}
          aria-label="Cerrar"
        >
          ×
        </button>
      )}
    </div>
  );
};

export default Alert;

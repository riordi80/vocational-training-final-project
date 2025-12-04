const Button = ({
  children,
  type = "button",
  variant = "primary",
  size = "md",
  disabled = false,
  onClick,
  className = "",
  fullWidth = false
}) => {
  const baseStyles = "font-medium rounded-md transition focus:outline-none focus:ring-2 focus:ring-offset-2";

  const variants = {
    primary: "bg-green-600 hover:bg-green-700 text-white focus:ring-green-500 disabled:bg-gray-400",
    secondary: "bg-gray-200 hover:bg-gray-300 text-gray-800 focus:ring-gray-500 disabled:bg-gray-100",
    danger: "bg-red-600 hover:bg-red-700 text-white focus:ring-red-500 disabled:bg-gray-400",
    outline: "border-2 border-green-600 text-green-600 hover:bg-green-50 focus:ring-green-500 disabled:border-gray-300 disabled:text-gray-300"
  };

  const sizes = {
    sm: "px-3 py-1.5 text-sm",
    md: "px-4 py-2 text-base",
    lg: "px-6 py-3 text-lg"
  };

  const widthClass = fullWidth ? "w-full" : "";

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseStyles} ${variants[variant]} ${sizes[size]} ${widthClass} ${className} disabled:cursor-not-allowed`}
    >
      {children}
    </button>
  );
};

export default Button;
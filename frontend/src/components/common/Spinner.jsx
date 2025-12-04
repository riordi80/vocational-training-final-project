const Spinner = ({
  size = "md",
  color = "green",
  text = ""
}) => {
  const sizes = {
    sm: "h-6 w-6",
    md: "h-12 w-12",
    lg: "h-16 w-16"
  };

  const colors = {
    green: "border-green-600",
    white: "border-white",
    gray: "border-gray-600"
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <div
        className={`${sizes[size]} animate-spin rounded-full border-b-2 ${colors[color]}`}
        role="status"
        aria-label="Cargando"
      />
      {text && (
        <p className="mt-4 text-gray-600">{text}</p>
      )}
    </div>
  );
};

export default Spinner;
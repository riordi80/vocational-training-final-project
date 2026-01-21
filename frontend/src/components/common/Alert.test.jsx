import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Alert from "./Alert";

describe("Alert Test", () => {
  test("No renderiza nada si no hay mensaje", () => {
    const { container } = render(<Alert />);
    expect(container.firstChild).toBeNull();
  });

  test("Muestra el mensaje correctamente", () => {
    render(<Alert message="Test message" />);
    expect(screen.getByText("Test message")).toBeInTheDocument();
  });

  test("Muestra el tipo 'success' con su icono", () => {
    render(<Alert type="success" message="Success!" />);
    expect(screen.getByText("✓")).toBeInTheDocument();
  });

  test("Muestra el tipo 'error' con su icono", () => {
    render(<Alert type="error" message="Error!" />);
    expect(screen.getByText("✕")).toBeInTheDocument();
  });

  test("Muestra el botón de cerrar cuando es dismissible", () => {
    const onClose = vi.fn();
    render(<Alert message="Test" dismissible={true} onClose={onClose} />);
    expect(screen.getByLabelText("Cerrar")).toBeInTheDocument();
  });

  test("No muestra el botón de cerrar cuando no es dismissible", () => {
    render(<Alert message="Test" dismissible={false} />);
    expect(screen.queryByLabelText("Cerrar")).not.toBeInTheDocument();
  });

  test("Llama a onClose cuando se hace click en cerrar", async () => {
    const onClose = vi.fn();
    const user = userEvent.setup();
    render(<Alert message="Test" onClose={onClose} />);

    await user.click(screen.getByLabelText("Cerrar"));
    expect(onClose).toHaveBeenCalledTimes(1);
  });

  test("Tiene el atributo role='alert'", () => {
    render(<Alert message="Test" />);
    expect(screen.getByRole("alert")).toBeInTheDocument();
  });
});
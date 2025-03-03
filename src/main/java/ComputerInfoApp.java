import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import javax.swing.*;
import java.awt.*;

public class ComputerInfoApp {

    public static void main(String[] args) {
        // Создаем объект SystemInfo для получения данных о системе
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

        // Получаем информацию об операционной системе
        String osName = operatingSystem.getFamily();
        String osVersion = operatingSystem.getVersionInfo().toString();

        // Получаем информацию о процессоре и памяти
        long totalMemory = memory.getTotal() / (1024 * 1024); // В мегабайтах
        long freeMemory = memory.getAvailable() / (1024 * 1024); // В мегабайтах

        // Получаем количество мониторов и размеры основного монитора
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        int monitorCount = screens.length;
        Rectangle screenSize = screens[0].getDefaultConfiguration().getBounds();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Получаем полное имя модуля текущего процесса
        String moduleName = System.getProperty("sun.java.command");

        // Создаем GUI
        JFrame frame = new JFrame("Информация о компьютере");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Заполняем текстовую область информацией
        textArea.append("Операционная система: " + osName + " " + osVersion + "\n");
        textArea.append("Количество мониторов: " + monitorCount + "\n");
        textArea.append("Размеры основного монитора: " + screenWidth + "x" + screenHeight + " пикселей\n");
        textArea.append("Полное имя модуля текущего процесса: " + moduleName + "\n");
        textArea.append("Общая память: " + totalMemory + " MB\n");

        // Добавляем интерактивный выбор единиц измерения для свободной памяти
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Выберите единицы измерения для свободной памяти:");
        String[] units = {"Байты", "Килобайты", "Мегабайты", "Гигабайты"};
        JComboBox<String> unitSelector = new JComboBox<>(units);
        JButton button = new JButton("Показать");

        panel.add(label);
        panel.add(unitSelector);
        panel.add(button);

        // Добавляем обработчик кнопки
        button.addActionListener(e -> {
            String selectedUnit = (String) unitSelector.getSelectedItem();
            long freeMemoryInUnits = convertToUnit(freeMemory, selectedUnit);
            textArea.append("Свободная физическая память (" + selectedUnit + "): " + freeMemoryInUnits + "\n");
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Конвертирует объем памяти в выбранные единицы измерения.
     *
     * @param memory   объем памяти в мегабайтах
     * @param unit     выбранная единица измерения
     * @return объем памяти в указанных единицах
     */
    private static long convertToUnit(long memory, String unit) {
        switch (unit) {
            case "Байты":
                return memory * 1024 * 1024;
            case "Килобайты":
                return memory * 1024;
            case "Мегабайты":
                return memory;
            case "Гигабайты":
                return memory / 1024;
            default:
                return memory;
        }
    }
}
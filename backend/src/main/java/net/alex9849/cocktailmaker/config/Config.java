package net.alex9849.cocktailmaker.config;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.context.ContextBuilder;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.library.pigpio.impl.PiGpioNativeImpl;
import com.pi4j.plugin.mock.platform.MockPlatform;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProviderImpl;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
import com.pi4j.plugin.mock.provider.serial.MockSerialProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpiProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpiProviderImpl;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.i2c.PiGpioI2CProvider;
import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider;
import com.pi4j.plugin.pigpio.provider.serial.PiGpioSerialProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class Config {
    @Value("${alex9849.app.isRaspberryPi}")
    private boolean isRaspberryPi;

    private Context gpioController;

    @Bean(destroyMethod = "shutdown")
    public Context getGpioController() {
        if(gpioController == null) {
            ContextBuilder ctxBuilder = Pi4J.newContextBuilder();
            if(isRaspberryPi) {
                //ctxBuilder.add(LinuxFsI2CProvider.newInstance());
                PiGpio piGpio = PiGpioNativeImpl.newInstance();
                ctxBuilder.add(PiGpioDigitalInputProvider.newInstance(piGpio));
                ctxBuilder.add(PiGpioDigitalOutputProvider.newInstance(piGpio));
                ctxBuilder.add(PiGpioSpiProvider.newInstance(piGpio));
                ctxBuilder.add(PiGpioSerialProvider.newInstance(piGpio));
                ctxBuilder.add(PiGpioI2CProvider.newInstance(piGpio));
                ctxBuilder.add(PiGpioPwmProvider.newInstance(piGpio));
            } else {
                ctxBuilder.add(MockDigitalInputProvider.newInstance());
                ctxBuilder.add(MockDigitalOutputProvider.newInstance());
                ctxBuilder.add(MockSpiProvider.newInstance());
                ctxBuilder.add(MockSerialProvider.newInstance());
                ctxBuilder.add(MockI2CProvider.newInstance());
                ctxBuilder.add(MockPwmProvider.newInstance());
            }
            gpioController = ctxBuilder.build();
        }
        return gpioController;
    }


}

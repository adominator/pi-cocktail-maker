package net.alex9849.cocktailmaker.payload.dto.gpio;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.alex9849.cocktailmaker.model.gpio.GpioBoard;
import net.alex9849.cocktailmaker.model.gpio.I2CGpioBoard;
import net.alex9849.cocktailmaker.model.gpio.LocalGpioBoard;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GpioBoardDto {

    private interface Id { long getId(); }
    private interface Name { @NotNull String getName(); }
    private interface PinCount { int getPinCount(); }
    private interface UsedPinCount { int getUsedPinCount(); }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @Getter @Setter @EqualsAndHashCode
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = LocalGpioBoardDto.Request.Create.class, name = "local"),
                @JsonSubTypes.Type(value = I2CGpioBoardDto.Request.Create.class, name = "i2c")
        })
        public static class Create implements Name {
            String name;

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        @Getter @Setter @EqualsAndHashCode
        public abstract static class Detailed implements Id, Name, PinCount, UsedPinCount {
            long id;
            String name;
            int pinCount;
            int usedPinCount;

            protected Detailed(GpioBoard gpioBoard) {
                BeanUtils.copyProperties(gpioBoard, this);
                pinCount = (gpioBoard.getMaxPin() - gpioBoard.getMinPin()) + 1;
                usedPinCount = (int) gpioBoard.getPins().stream().filter(x -> x.getResource() != null).count();
            }

            public static Detailed toDto(GpioBoard gpioBoard) {
                if(gpioBoard instanceof LocalGpioBoard localGpioBoard) {
                    return new LocalGpioBoardDto.Response.Detailed(localGpioBoard);
                } else if (gpioBoard instanceof I2CGpioBoard i2CGpioBoard) {
                    return new I2CGpioBoardDto.Response.Detailed(i2CGpioBoard);
                } else {
                    throw new IllegalArgumentException("Unknown GpioBoard type: " + gpioBoard.getClass().getName());
                }
            }

            public abstract String getType();

        }

    }
}

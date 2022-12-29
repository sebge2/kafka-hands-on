package be.sgerard.kafka.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WikimediaEventDto {

   private String id;
   private String title;
   private String user;
   private boolean bot;
   private String serverName;
   private String comment;

}

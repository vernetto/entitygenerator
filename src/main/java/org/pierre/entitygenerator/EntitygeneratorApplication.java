package org.pierre.entitygenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
public class EntitygeneratorApplication implements CommandLineRunner {

	private final LinkEntityRepository linkEntityRepository;
	private final GroupEntityRepository groupEntityRepository;

	public EntitygeneratorApplication(LinkEntityRepository linkEntityRepository, GroupEntityRepository groupEntityRepository) {
		this.linkEntityRepository = linkEntityRepository;
        this.groupEntityRepository = groupEntityRepository;
    }

	public static void main(String[] args) {
		SpringApplication.run(EntitygeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//populateDB();
		List<GroupEntity> allGroups = groupEntityRepository.findAll();
		allGroups.forEach(groupEntity -> {
			log.info(groupEntity.toString());
		});

	}

	private void populateDB() {
		LinkEntity linkEntity = new LinkEntity();
		linkEntity.setName("one");
		GroupEntity groupEntity = new GroupEntity();
		groupEntity.setLinkEntity(linkEntity);
		groupEntity.setName("oneGroup");
		linkEntityRepository.save(linkEntity);
		groupEntityRepository.save(groupEntity);
	}
}

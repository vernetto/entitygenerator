package org.pierre.entitygenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
		populateDB();
		dumpDb();
		generateEntities();

	}

	private void generateEntities() throws ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		List<GroupEntity> allGroups = groupEntityRepository.findAll();
		String lineBreak = "\n";
		sb.append(lineBreak);
		sb.append("List<LinkEntity> links = new ArrayList();").append(lineBreak);
		sb.append("List<GroupEntity> groups = new ArrayList();").append(lineBreak);
		AtomicInteger count = new AtomicInteger(0);
		Class<?> clazzLinkEntity = Class.forName("org.pierre.entitygenerator.LinkEntity");
		Class<?> clazzGroupEntity = Class.forName("org.pierre.entitygenerator.GroupEntity");
		for (Field field : clazzLinkEntity.getDeclaredFields()) {
			log.info("Link:" + field.getName());
		}
		for (Field field : clazzGroupEntity.getDeclaredFields()) {
			log.info("Group: " + field.getName());
		}

		allGroups.forEach(groupEntity -> {
			sb.append(lineBreak);
			int beanCount = count.get();
			sb.append("LinkEntity lintEntity").append(beanCount).append(" = LinkEntity.builder()").append(lineBreak);

			sb.append(".name(\"").append(groupEntity.getLinkEntity().getName()).append("\")").append(lineBreak);
			sb.append(".build();").append(lineBreak);
			sb.append("links.add(lintEntity").append(beanCount).append(");").append(lineBreak);

			sb.append("GroupEntity groupEntkity").append(beanCount).append(" = GroupEntity.builder()").append(lineBreak);
			sb.append(".name(\"").append(groupEntity.getName()).append("\")").append(lineBreak);
			sb.append(".linkEntity(lintEntity").append(beanCount).append(").build());").append(lineBreak);
			sb.append("groups.add(groupEntity").append(beanCount).append(");").append(lineBreak);
			count.getAndIncrement();

		});
		log.info(sb.toString());

	}

	private void dumpDb() {
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

	private void repopulateDB(){
		List<LinkEntity> links = new ArrayList();
		List<GroupEntity> groups = new ArrayList();

		LinkEntity lintEntity0 = LinkEntity.builder()
				.name("one")
				.build();
		groups.add(GroupEntity.builder()
				.name("oneGroup")
				.linkEntity(lintEntity0).build());

	}
}

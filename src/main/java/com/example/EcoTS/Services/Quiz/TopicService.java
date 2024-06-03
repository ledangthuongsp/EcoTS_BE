package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.Topic;
import com.example.EcoTS.Repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    public Topic createTopic(Topic topic) {
        topic.setProgress(0.0); // Initialize progress
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    public void updateProgress(Long topicId, int correctAnswers, int totalQuestions) {
        Topic topic = getTopicById(topicId);
        if (topic != null) {
            double progress = ((double) correctAnswers / totalQuestions) * 100;
            topic.setProgress(progress);
            updateTopic(topic);
        }
    }
}

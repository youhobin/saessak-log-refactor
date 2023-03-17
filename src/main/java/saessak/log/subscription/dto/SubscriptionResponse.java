package saessak.log.subscription.dto;


public class SubscriptionResponse {

    private Long subscriptionId;
    private boolean isSubscribed;

    public SubscriptionResponse(Long subscriptionId, boolean isSubscribed) {
        this.subscriptionId = subscriptionId;
        this.isSubscribed = isSubscribed;
    }
}

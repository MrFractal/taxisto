#import "NSMutableArray.h"
#import "NSMutableArray.h"

@interface CardsInfoResponse : NSObject {
  NSMutableArray * cardInfo;
}

@property(nonatomic, retain, readonly) NSMutableArray * cardInfo;
- (void) init;
@end
